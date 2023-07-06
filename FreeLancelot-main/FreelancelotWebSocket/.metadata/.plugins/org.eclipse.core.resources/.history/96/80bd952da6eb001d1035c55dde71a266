package UserDetailsHelper;

import model.JobDetails;
import model.ProjectInfo;
import model.UserInformation;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class UserDetails {


    public JSONObject getInformationFromConnection(String urlString)
    {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                Scanner scan = new Scanner(url.openStream());
                String temp = "";
                while (scan.hasNext()) {
                    temp = temp + scan.nextLine();
                }
                JSONObject json = new JSONObject(temp);
                JSONObject jsonObject = json.getJSONObject("result");
                return jsonObject;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Method to get owner id details
     * @param ownerId Owner ID
     * @return UserInformation object
     * @author Almas Saba
     */

    public UserInformation getUserDetails(String ownerId)
    {

        UserInformation userInformation = new UserInformation();
        JSONObject jsonObject = this.getInformationFromConnection("https://www.freelancer.com/api/users/0.1/users/"+ownerId+"?avatar=true&display_info=true&country_details=true&jobs=true&portfolio_details=true&preferred_details=true&profile_description=true&qualification_details=true&recommendations=true&responsiveness=true&status=true&&operating_areas=true&compact=true");

        if(jsonObject != null) {

            userInformation.setId(jsonObject.get("id").toString());
            userInformation.setUsername(jsonObject.get("username").toString());

            userInformation.setRegistrationDate(jsonObject.get("registration_date").toString());

            userInformation.setLimitedAccount(jsonObject.get("limited_account").toString());

            userInformation.setRole(jsonObject.get("role").toString());

            if(jsonObject.has("status"))
            {
                JSONObject status = jsonObject.getJSONObject("status");
                userInformation.setEmailVerified(status.get("email_verified").toString());

            }
            else
            {
                userInformation.setEmailVerified("Not given");
            }


            if(jsonObject.has("primary_currency"))
            {
                JSONObject primaryCurrency = jsonObject.getJSONObject("primary_currency");
                userInformation.setPrimaryCurrency((primaryCurrency.get("name").toString()));

            }
            else
            {
                userInformation.setPrimaryCurrency("Not given");
            }


            if(jsonObject.has("public_name"))
            {
                if(jsonObject.get("public_name").toString().isEmpty())
                {
                    userInformation.setCompany("Not given");
                }
                else
                {
                    userInformation.setCompany(jsonObject.get("public_name").toString());
                }

            }
            else
            {
                userInformation.setCompany("Not given");
            }

            JSONObject location = jsonObject.getJSONObject("location");
            if(location.has("country"))
            {
                JSONObject country = location.getJSONObject("country");

                if(country.get("name").toString().isEmpty())
                {
                    userInformation.setCountry("Not given");
                }
                else
                {
                    userInformation.setCountry(country.get("name").toString());
                }
            }
            else
            {
                userInformation.setCountry("Not given");
            }

        }
        else
        {
            userInformation.setId("Not found");
            userInformation.setUsername("Not found");

            userInformation.setLimitedAccount("Not found");

            userInformation.setRole("Not found");

            userInformation.setEmailVerified("Not Found");

            userInformation.setPrimaryCurrency("Not found");

            userInformation.setCompany("Not found");

            userInformation.setCountry("Not found");
        }

        JSONArray projects = null;
        JSONArray requiredSkills = null;

        jsonObject = this.getInformationFromConnection("https://www.freelancer.com/api/projects/0.1/projects/?owners[]="+ownerId+"&limit=10&job_details=true&compact=true&full_description=true");

        if (jsonObject != null) {

            projects = jsonObject.getJSONArray("projects");

            JSONObject projectsElements;
            JSONObject requiredSkillElements;

            ArrayList<ProjectInfo> projectDetailsList = new ArrayList<>();

            for (int i = 0; i < projects.length(); i++) {

                ProjectInfo projectDetails = new ProjectInfo();

                projectsElements = projects.getJSONObject(i);
                projectDetails.setOwnerId(projectsElements.get("owner_id").toString());

                projectDetails.setTimeSubmitted(projectsElements.get("time_submitted").toString());

                projectDetails.setTitle(projectsElements.get("title").toString());

                projectDetails.setType(projectsElements.get("type").toString());
                requiredSkills = projectsElements.getJSONArray("jobs");
                JobDetails[] skillsList = new JobDetails[requiredSkills.length()];

                for (int j = 0; j < requiredSkills.length(); j++) {
                    JobDetails jobDetails = new JobDetails();
                    requiredSkillElements = requiredSkills.getJSONObject(j);
                    jobDetails.setSkillName(requiredSkillElements.get("name").toString());
                    skillsList[j] = jobDetails;

                }
                projectDetails.setSkills(skillsList);
                projectDetails.setPreviewDescription(projectsElements.get("preview_description").toString());
                projectDetailsList.add(projectDetails);

            }
            userInformation.setProjectList(projectDetailsList);

        }
        else
        {
            userInformation.setProjectList(null);
        }

        return userInformation;
    }
}