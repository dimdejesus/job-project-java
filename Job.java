import java.lang.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;

public class Job {

	private String jobNumber;
	private String jobTitle;
	private String jobPosterName;
	private String jobPosterAddress;
	private String jobPostedDate;
	private String jobExperienceLevel;
	private String jobType;
	private String [] jobRequiredSkills;
	private double jobSalary;
	private String jobDescription;

	public Job (String number, String title, String postername, String posteraddress, String date,
		String experiencelevel, String type, String [] skills, double salary, String description)
	{
			this.jobNumber = number;
			this.jobTitle = title;
			this.jobPosterName = postername;
			this.jobPosterAddress = posteraddress;
			this.jobPostedDate = date;
			this.jobExperienceLevel = experiencelevel;
			this.jobType = type;
			this.jobRequiredSkills = skills;
			this.jobSalary = salary;
			this.jobDescription = description;

	}

	public boolean addJob()
	{
		boolean validate = this.validateInput(
			this.jobNumber, 
			this.jobTitle, 
			this.jobPosterName, 
			this.jobPosterAddress, 
			this.jobPostedDate, 
			this.jobExperienceLevel, 
			this.jobType, 
			this.jobRequiredSkills, 
			this.jobSalary, 
			this.jobDescription
		);

		if(!validate) return false;

		this.printTxt(
			this.jobNumber, 
			this.jobTitle, 
			this.jobPosterName, 
			this.jobPosterAddress, 
			this.jobPostedDate, 
			this.jobExperienceLevel, 
			this.jobType, 
			this.jobRequiredSkills, 
			this.jobSalary, 
			this.jobDescription
		);

		return true;
	}

	public boolean updateJob (String jobNum, String title, String posterName, String posterAddress, String date, String experienceLevel,
		String type, String[] skills, double salary, String description)
	{
			// update the info of a given job in a txt file
			boolean validate = this.validateInput(jobNum, title, posterName, posterAddress, date, experienceLevel, type, skills, salary, description);

			if(!validate) return false;

			String jobData = this.search(jobNum);

			if(jobData == "") return false;

			String[] job = jobData.split(",");

			double salaryIncrease = (Integer.parseInt(job[8]) - salary) / salary;

			if((salaryIncrease < 0.2 || salaryIncrease > 0.4) && experienceLevel != "Junior") return false;
			else if(salaryIncrease > 0.1 && experienceLevel == "Junior") return false;

			if(job[6] == "Full-Time" && (type == "Part-Time" || type == "Volunteer")) 
				return false;

			this.updateTxtFile(jobNum, title, posterName, posterAddress, date, experienceLevel, type, skills, salary, description);

			return true;
	}

	private boolean validateInput(String jobNum, String title, String posterName, String posterAddress, String date, String experienceLevel,
		String type, String[] skills, double salary, String description) 
	{
		// VALIDATE JOB NUMBER
		// Pattern that accepts first 5 characters (1 to 5) 6th to 8th as capital letter A to Z and a special character
		String pattern = "[1-5]{5}[A-Z]{3}[!@#$%^&*]";
		boolean jobNumValidate = this.validateRegex(pattern, jobNum);
		// Perform a condition if the pattern is met
		if(!jobNumValidate) return false;

		// VALIDATE JOB DATE
		SimpleDateFormat dateInputFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateParsed = null;

		try {
			dateParsed = dateInputFormat.parse(date);
				if (!date.equals(dateInputFormat.format(dateParsed))) {
					dateParsed = null;
				}
		} catch (ParseException e) {
			dateParsed = null;
			e.printStackTrace();
		}

		if(dateParsed == null) return false;

		// VALIDATE ADDRESS
		pattern = "^[^,]+,[^,]+,[^,]+$";
		boolean addressValidate = this.validateRegex(pattern, posterAddress);
		// Perform a condition if the pattern is met
		if(!addressValidate) return false;

		// VALIDATE EXPERIENCE LEVEL SALARY
		if(!(salary > 100000) || experienceLevel != "Senior" && experienceLevel != "Executive") {
			return false;
		} else if (!(salary > 40000 && salary < 70000) || experienceLevel != "Junior") {
			return false;
		} else if(!(salary > 70000 && salary < 100000) || experienceLevel != "Medium") {
			return false;
		}

		// VALIDATE TYPE
		if(type == "Part-Time" &&  (experienceLevel == "Senior" || experienceLevel == "Executive")) {
			return false;
		}

		// VALIDATE SKILLS
		for(String skill : skills) {
			boolean skillValidateWord = this.validateSkillWord(skill);
			if(!skillValidateWord) return false;
		}

		return true;
	}

	private boolean validateRegex(String pattern, String value)
	{
		// Create a Pattern object
		Pattern regex = Pattern.compile(pattern);
		// Create a Matcher object
		Matcher matcher = regex.matcher(value);

		// Perform a condition if the pattern is met
		if(!matcher.matches()) return false;

		return true;
	}

	private boolean validateSkillWord(String word) {
		String[] wordArr = word.split(",");
		if(wordArr.length > 3 || wordArr.length < 1) {
			return false;
		}

		return true;
	}

	private String search(String jobNum) {
		try (BufferedReader reader = new BufferedReader(new FileReader("job.txt"))) {
			String line;
			while((line = reader.readLine()) != null) {
				String[] jobData = line.split(",");
				if(jobData[0] == jobNum) return String.join(",", jobData);
			}
			return "";
		} catch (IOException e) {
			System.out.println("ERROR!");
			return "";
		}
	}

	private boolean updateTxtFile(String jobNum, String title, String posterName, String posterAddress, String date, String experienceLevel,
		String type, String[] skills, double salary, String description) {
		try {
			File originalFile = new File("job.txt");
			BufferedReader reader = new BufferedReader(new FileReader(originalFile));

			File tempFile = new File("temp.txt");
      BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			String line;
			while((line = reader.readLine()) != null) {
				String[] jobData = line.split(",");
				if(jobData[0] != jobNum) {
					this.printTxt(jobNum, title, posterName, posterAddress, date, experienceLevel, type, skills, salary, description);
				} else {
					writer.write(line);
				}
			}
			return true;
		} catch (IOException e) {
			System.out.println("ERROR!");
			return false;
		}
	}

	private boolean printTxt(String jobNum, String title, String posterName, String posterAddress, String date, String experienceLevel,
		String type, String[] skills, double salary, String description) 
	{
		try (FileWriter writer = new FileWriter("job.txt", true)) {
			writer.write(String.format(
				"%s|%s|%s|%s|%s|%s|%s|%s|%.2f|%s",
				jobNum,
				title,
				posterName,
				posterAddress,
				date,
				experienceLevel,
				type,
				String.join(",", skills),
				salary,
				description
			));

			System.out.println("Successfully written to text file!");
			return true;
		} catch(IOException e){
			System.out.println("Error!");
			return false;
		}
	}
}