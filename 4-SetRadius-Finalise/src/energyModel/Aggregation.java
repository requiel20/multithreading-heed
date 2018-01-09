package energyModel;

import core.Node;

public interface Aggregation {
	public int numberOfintraClusterAggregatedMessages(Node chNode);
	public int numberOfinterClusterAggregatedMessages(Node chNode);
}
