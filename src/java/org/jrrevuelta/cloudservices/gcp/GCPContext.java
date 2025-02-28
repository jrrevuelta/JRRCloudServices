package org.jrrevuelta.cloudservices.gcp;

import org.jrrevuelta.cloudservices.CloudServiceContext;

public class GCPContext implements CloudServiceContext {
	
	private String projectId;
	private String locationId;
	
	
	public GCPContext() { super(); }
	
	public String getProjectId() { return projectId; }
	public void setProjectId(String projectId) { this.projectId = projectId; }
	
	public String getLocationId() { return locationId; }
	public void setLocationId(String locationId) { this.locationId = locationId; }
	
}
