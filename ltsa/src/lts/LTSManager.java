/* AMES: Enhanced Modularity */
package lts;

import java.util.*;

public interface LTSManager {
	void performAction(Runnable r, boolean showOutputPane);
	String getTargetChoice();
	CompositeState compile(String name);
	void newMachines(List<CompactState> machines);
	Set<String> getLabelSet(String name);
}
