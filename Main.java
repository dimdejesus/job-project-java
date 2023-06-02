import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Please Choose (1) Add Job (2) Update Job: ");
		String action = scanner.nextLine();

		System.out.print("Enter Job Number: ");
		String jobNumber = scanner.nextLine();

		System.out.print("Enter Job Title: ");
		String jobTitle = scanner.nextLine();

		System.out.print("Enter Job Poster: ");
		String jobPoster = scanner.nextLine();

		System.out.print("Enter Job Poster Address: ");
		String jobPosterAddress = scanner.nextLine();

		System.out.print("Enter Job Post: ");
		String jobPosterDate = scanner.nextLine();

		System.out.print("Enter Experience Level: ");
		String jobExperienceLevel = scanner.nextLine();

		System.out.print("Enter Job Type: ");
		String jobType = scanner.nextLine();

		System.out.print("Enter Job Required skills (Separated by comma and maximum of 2 words each): ");
		String jobRequiredSkils = scanner.nextLine();

		System.out.print("Enter Job Salary: ");
		double jobSalary = scanner.nextDouble();

		System.out.print("Enter Job Description: ");
		String jobDescription = scanner.nextLine();

		Job job = new Job(jobNumber, jobTitle, jobPoster, jobPosterAddress, jobPosterDate, jobExperienceLevel, jobType, jobRequiredSkils.split(","), jobSalary, jobDescription);

		job.addJob();

		scanner.close();
	}
}
