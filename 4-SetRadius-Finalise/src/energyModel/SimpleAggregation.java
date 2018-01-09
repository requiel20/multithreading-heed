package energyModel;

import core.Node;

public class SimpleAggregation implements Aggregation{

	/**
	 * The CH will receive N messages from its cluster members and it will
	 * forward exactly one messages. We have the best aggreagtion.
	 */
	public int numberOfintraClusterAggregatedMessages(Node chNode) {
		return 1;
	}

	/**
	 * There is no aggreagation of inter-traffic. This is equal to
	 * the inter_traffic_load_factor (the node forwards all the messages 
	 * received from CH downwards).
	 */
	public int numberOfinterClusterAggregatedMessages(Node chNode) {
		return chNode.inter_traffic_load_factor;
	}



}
