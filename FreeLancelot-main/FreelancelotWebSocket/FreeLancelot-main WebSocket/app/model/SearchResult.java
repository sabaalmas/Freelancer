package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchResult {

    String keyword;
    List<ProjectInfo> ProjectInfo;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<ProjectInfo> getProjectInfo() {
        return ProjectInfo;
    }

    public void setProjectInfo(List<ProjectInfo> projectInfo) {
        ProjectInfo = projectInfo;
    }
    
    public List<ProjectInfo> getFirstTenProjectInfo() {
        return ProjectInfo.stream().limit(10).collect(Collectors.toList());
    }
    
	public int getSizeOfList() {
		return ProjectInfo.size();
	}
    
    public void addProjectInfo(List<ProjectInfo> projectInfo)
    {
    	List<ProjectInfo> projectInfoTmp = new ArrayList<>();
    	projectInfoTmp.addAll(this.ProjectInfo);
    	projectInfoTmp.addAll(projectInfo);
    	this.ProjectInfo=projectInfoTmp;
    }
}
