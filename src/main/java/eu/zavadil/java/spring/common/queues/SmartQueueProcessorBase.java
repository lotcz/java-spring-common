package eu.zavadil.java.spring.common.queues;

import eu.zavadil.java.queues.SmartQueue;
import eu.zavadil.java.queues.SmartQueueProcessor;
import eu.zavadil.java.queues.SmartQueueProcessorState;
import eu.zavadil.java.queues.SmartQueueProcessorStats;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class SmartQueueProcessorBase<T> implements SmartQueueProcessor<T> {

	@Getter
	private final SmartQueue<T> queue;

	public SmartQueueProcessorBase(SmartQueue<T> queue) {
		this.queue = queue;
	}

	@Getter
	private SmartQueueProcessorState state = SmartQueueProcessorState.Idle;

	public abstract void processItem(T e);

	@Override
	public void process() {
		this.state = SmartQueueProcessorState.Processing;
		while (this.queue.hasNext()) {
			log.info("Processing document queue, {} remaining", this.queue.getRemaining());
			this.processItem(this.queue.next());
		}
		log.info("Document queue empty");
		this.queue.reset();
		this.state = SmartQueueProcessorState.Idle;
	}

	public SmartQueueProcessorStats getStats() {
		return new SmartQueueProcessorStats(
			this.queue.getRemaining(),
			this.queue.getLoaded(),
			this.queue.isLoading() ? SmartQueueProcessorState.Loading : this.state
		);
	}
}
