package model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectInfo {
    @JsonProperty("title")
    String title;
    @JsonProperty("type")
    String type;
    @JsonProperty("owner_id")
    String ownerId;
    @JsonProperty("jobs")
    JobDetails skills[];
    @JsonProperty("preview_description")
    String previewDescription;
    @JsonProperty("time_submitted")
    String timeSubmitted;



    public String getTimeSubmitted() {
		return timeSubmitted;
	}

	public void setTimeSubmitted(String timeSubmitted) {
		Date date = new Date(Long.parseLong(timeSubmitted+"000"));

		String pattern = "MMM-dd-yyyy";

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		String dateSubitted = simpleDateFormat.format(date);
		this.timeSubmitted = dateSubitted;
	}

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public JobDetails[] getSkills() {
        return skills;
    }

    public void setSkills(JobDetails[] skills) {
        this.skills = skills;
    }
    
    public String getPreviewDescription() {
        return previewDescription;
    }

    public void setPreviewDescription(String previewDescription) {
        this.previewDescription = previewDescription;
    }
}
