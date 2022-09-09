package legion;

import legion.serviceFacade.rmi.ObjectModelRemote;

public interface ObjectModelInfo {
	public String getUid();

	public long getObjectCreateTime();

	public long getObjectUpdateTime();
	
}
