import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class HomeMenu{
	Socket socket;
	PrintWriter out;
	BufferedReader in;
	Connection c;
	Statement s;

	HomeMenu(Socket socket,BufferedReader in,PrintWriter out) throws IOException {
		this.socket= socket;	
		this.out = out;
		this.in = in;
	}


	public void menu() throws IOException, ClassNotFoundException, SQLException{	

		while(true) {
			out.println("Menu\nenter your option");
			out.flush();
			out.println("1. Login..");
			out.flush();
			out.println("2. Register..");
			out.flush();
			//out.println("3. Exit");

			out.println("end");
			out.flush();

			String str = in.readLine();

			Duties(str);

		}
	}

	public void Duties(String str) throws ClassNotFoundException, SQLException, IOException{

		if(str.equals("1")){
			Login();

		}
		else if(str.equals("2")){
			Register();
		}
		/*	if(str.equals("3")){
			break;
		}*/
	}

	private void Register() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub


		try {

			out.println("Enter Name");
			out.flush();
			String name = in.readLine();
			//.out.println(name);

			out.println("User Name");
			out.flush();
			String username = in.readLine();
			//System.out.println(username);

			out.println("password");
			out.flush();
			String password = in.readLine();
			//System.out.println(password);

			out.println("Role(student/teacher)");
			out.flush();
			String Role = in.readLine();
			//System.out.println(Role);

			InsertRegister(name,username,password,Role);
			//System.out.println("pinky2");

			//	out.println("end");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void InsertRegister(String name, String username, String password,String role) throws ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub
		//	String query ="select * from Register where username="+username;

		DB db= new DB();
		c = db.conn;
		s = db.stmt;
		//System.out.println("pinky3");

		PreparedStatement ps = c.prepareStatement("select username from registration where username=?");

		ps.setString(1,username);
		//ps.setString(2, request.getParameter("pwd"));


		ResultSet rs = ps.executeQuery();

		if(rs.next())
		{
			out.println("user already exists register again..(press any key to continue)");
			out.flush();
			in.readLine();
			//	out.println("press any key to get the menu");
		}
		else{
			PreparedStatement p = c.prepareStatement("insert into registration values(?,?,?,?)");

			p.setString(1,name);
			p.setString(2,username);
			p.setString(3,password);
			p.setString(4,role);

			p.executeUpdate();

		}

	}

	private void Login() throws IOException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub

		DB db= new DB();
		c = db.conn;
		s = db.stmt;

		out.println("enter username");
		out.flush();
		String username = in.readLine();

		out.println("enter password");
		out.flush();
		String password = in.readLine();

		PreparedStatement ps = c.prepareStatement("select Role from registration where username=? and password=?");

		ps.setString(1,username);
		ps.setString(2,password);


		ResultSet rs = ps.executeQuery();

		if(rs.next())
		{
			String role = rs.getString("Role");
			System.out.println(role);

			if(role.equals("teacher")){

				TeacherRoles(username);

			}else{
				StudentRoles(username);
			}

		}
		else{

			out.println("Wrong Credentials...(press any key to continue)");
			out.flush();
			in.readLine();

			//out.println("press any key to get menu");
		}
	}

	private void StudentRoles(String username) throws IOException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		System.out.println("student");
		while(true){
			out.println("menu\n1.Attempt Quiz");
			out.flush();
			out.println("2.Take random Quiz");
			out.flush();
			out.println("3.Logout");
			out.flush();
			out.println("end");
			out.flush();

			String opt=in.readLine();
			if(opt.equals("1")){
				AttemptQuiz(username);
			}
			if(opt.equalsIgnoreCase("2")){
				GenerateRandomQuiz(username);

			}
			if(opt.equalsIgnoreCase("3")){
				break;

			}
		}

	}

	private void GenerateRandomQuiz(String username) throws ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub
		DB db= new DB();
		c = db.conn;
		s = db.stmt;

		int score = 0,pa=0,na=0;

		PreparedStatement p = c.prepareStatement("select * from questions");

		ResultSet s = p.executeQuery();
		String[] questions ;
		int i=0;
		questions=new  String[100];
		while(s.next()){
			questions[i] = s.getString("question");
			i++;
		}

		ArrayList<Integer> qasked = new ArrayList<Integer>();

		for (int j = 0; j < i; j++) {

			int k = (int) (Math.random()*1000 % i);

			while(qasked.contains(k)){

				k = (int) (Math.random()*1000 % i);
			}
			qasked.add(k);
			System.out.println("index is"  +k);
			System.out.println("random quest is"+questions[k]);
			PreparedStatement t = c.prepareStatement("select * from questions where question=?");

			t.setString(1,questions[k]);


			ResultSet rquestions = t.executeQuery();



			while(rquestions.next()){
				out.println("menu\n");
				out.flush();
				out.println("Q. "+rquestions.getString("question"));
				out.flush();
				out.println("\t1. "+rquestions.getString("option1"));
				out.flush();
				out.println("\t2. "+rquestions.getString("option2"));
				out.flush();
				out.println("\t3. "+rquestions.getString("option3"));
				out.flush();
				out.println("\t4. "+rquestions.getString("option4"));
				out.flush();
				out.println("\t5. type skip for next questions");
				out.flush();
				out.println("end");
				out.flush();

				String rightanswer = rquestions.getString("rightanswer");

				String answer = in.readLine();

				if(answer.equalsIgnoreCase("skip")){

				}else if(answer.equalsIgnoreCase(rightanswer)){
					score = score+1;
					pa++;
				}else{
					//score = score+nw;
					na++;
				}
			}
		}
		PreparedStatement r = c.prepareStatement("insert into reports values(?,?,?,?,?)");

		r.setString(1,username);
		r.setString(2,"randomquiz");
		r.setInt(3,pa);
		r.setInt(4,na);
		r.setInt(5,score);


		r.executeUpdate();


		out.println("menu\nyour Score for the randomquiz is :"+score);
		out.println("no of correct answers:"+pa);
		out.println("no of wrong answers:"+na);
		out.println("press any key to go to menu");
		out.println("end");
		in.readLine();

	}






	private void AttemptQuiz(String username) throws ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub

		DB db= new DB();
		c = db.conn;
		s = db.stmt;

		PreparedStatement p = c.prepareStatement("select quizname from quiz");

		ResultSet s = p.executeQuery();

		out.println("menu\nenter name of a quiz to take the quiz");
		out.flush();
		int i=1;
		while(s.next()){
			out.println(i+"."+s.getString("quizname"));
			out.flush();
			i++;
		}
		out.println("end");
		out.flush();

		String quiz = in.readLine();

		ViewQuiz(username,quiz);
	}

	private void ViewQuiz(String username, String quiz) throws ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub

		DB db= new DB();
		c = db.conn;
		s = db.stmt;
		int score = 0,pa=0,na=0;

		PreparedStatement p = c.prepareStatement("select * from quiz where quizname=?");

		p.setString(1, quiz);

		ResultSet s = p.executeQuery();

		while(s.next()){
			out.println("menu\n");
			out.flush();
			out.println("Q. "+s.getString("question"));
			out.flush();
			out.println("\t1. "+s.getString("option1"));
			out.flush();
			out.println("\t2. "+s.getString("option2"));
			out.flush();
			out.println("\t3. "+s.getString("option3"));
			out.flush();
			out.println("\t4. "+s.getString("option4"));
			out.flush();
			out.println("\t5. type skip for next questions");
			out.flush();
			out.println("end");
			out.flush();

			String rightanswer = s.getString("rightanswer");
			int pw = s.getInt("positiveweightage");
			int nw = s.getInt("negativeweightage");

			String answer = in.readLine();

			if(answer.equalsIgnoreCase("skip")){

			}else if(answer.equalsIgnoreCase(rightanswer)){
				score = score+pw;
				pa++;
			}else{
				score = score+nw;
				na++;
			}
		}

		PreparedStatement r = c.prepareStatement("insert into reports values(?,?,?,?,?)");

		r.setString(1,username);
		r.setString(2,quiz);
		r.setInt(3,pa);
		r.setInt(4,na);
		r.setInt(5,score);


		r.executeUpdate();


		out.println("menu\nyour Score for the quiz "+quiz+" is :"+score);
		out.println("no of correct answers:"+pa);
		out.println("no of wrong answers:"+na);
		out.println("press any key to go to menu");
		out.println("end");
		in.readLine();

	}

	private void TeacherRoles(String username) throws IOException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		System.out.println("teacher");

		String user;
		user =username;

		while(true){
			out.println("menu\n 1.Add Subjects");
			out.flush();
			out.println("2.Add Chapters");
			out.flush();
			out.println("3.Add Questions");
			out.flush();
			out.println("4.Make quiz");
			out.flush();
			out.println("5.Logout");
			out.flush();

			out.println("end");
			out.flush();

			String p = in.readLine();

			if(p.equals("1")){
				AddSubjects(user);
			}if(p.equals("2")){
				AddChapters(user);

			}if(p.equals("3")){
				AddQuestions(user);
			}if(p.equals("4")){
				MakeQuiz(user);

			}if(p.equals("5")){
				break;

			}
		}
	}

	private void MakeQuiz(String user) throws ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub
		DB db= new DB();
		c = db.conn;
		s = db.stmt;

		out.println("Enter Subject name");
		out.flush();
		String subject = in.readLine();

		out.println("Enter Chapter name");
		out.flush();
		String chapter = in.readLine();

		out.println("Enter quiz name");
		out.flush();
		String quizname = in.readLine();

		PreparedStatement ps = c.prepareStatement("select subject from quiz where username=? and subject=? and chapter=? and quizname=?");

		ps.setString(1,user);
		ps.setString(2,subject);
		ps.setString(3,chapter);
		ps.setString(4,quizname);
		ResultSet rs = ps.executeQuery();

		if(rs.next()){
			out.println("quiz already exists or no chapter or no subject(press any key");
			out.flush();
			in.readLine();

		}else{

			out.println("Enter positive weightage");
			out.flush();
			int pw = Integer.parseInt(in.readLine());

			out.println("Enter negative weightage");
			out.flush();
			int nw = Integer.parseInt(in.readLine());

			out.println("Enter no.of questions");
			out.flush();
			int n = Integer.parseInt(in.readLine());

			int i=0;


			PreparedStatement s =c.prepareStatement( "select * from questions where username=? and subject=? and chapter=?");
			s.setString(1,user);
			s.setString(2,subject);
			s.setString(3,chapter);

			ResultSet sr = s.executeQuery();

			while(sr.next() && i<n){

				String qus = sr.getString("question");
				System.out.println(qus);
				String opt1 = sr.getString("option1");
				System.out.println(opt1);
				String opt2 = sr.getString("option2");
				String opt3 = sr.getString("option3");
				String opt4 = sr.getString("option4");
				String rtopt = sr.getString("rightanswer");

				PreparedStatement p = c.prepareStatement("insert into quiz values(?,?,?,?,?,?,?,?,?,?,?,?)");

				p.setString(1,user);
				p.setString(2,quizname);
				p.setString(3,subject);
				p.setString(4,chapter);
				p.setInt(5, pw);
				p.setInt(6, nw);
				p.setString(7,qus);
				p.setString(8,opt1);
				p.setString(9,opt2);
				p.setString(10,opt3);
				p.setString(11,opt4);
				p.setString(12, rtopt);


				p.executeUpdate();

				i++;

			}

		}	
	}

	private void AddQuestions(String user) throws ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub

		DB db= new DB();
		c = db.conn;
		s = db.stmt;

		out.println("Enter Subject name");
		out.flush();

		String subject = in.readLine();

		out.println("Enter Chapter name");
		out.flush();

		String chapter = in.readLine();

		String question;
		PreparedStatement ps = c.prepareStatement("select subject from chapter where username=? and subject=? and chapter=?");

		ps.setString(1,user);
		ps.setString(2,subject);
		ps.setString(3, chapter);

		ResultSet rs = ps.executeQuery();

		if(rs.next())
		{
			out.println("menu\nSubject and chapter exists..");
			out.flush();
			out.println("add question..");
			out.flush();
			out.println("end");

			question = in.readLine();

			PreparedStatement p = c.prepareStatement("select question from questions where username=? and subject=? and chapter=? and question=?");

			p.setString(1,user);
			p.setString(2,subject);
			p.setString(3,chapter);
			p.setString(4, question);

			ResultSet r = p.executeQuery();

			if(r.next()){
				out.println("Question already exists.. try adding other questions..(press any key to continue)");
				out.flush();
				in.readLine();
			}else{

				out.println("enter option1");
				out.flush();
				String option1 = in.readLine();

				out.println("enter option2");
				out.flush();
				String option2 = in.readLine();

				out.println("enter option3");
				out.flush();
				String option3 = in.readLine();

				out.println("enter option4");
				out.flush();
				String option4 = in.readLine();

				out.println("enter Right Option");
				out.flush();
				String rtopt = in.readLine();


				PreparedStatement s = c.prepareStatement("insert into questions values(?,?,?,?,?,?,?,?,?)");

				s.setString(1,user);
				s.setString(2,subject);
				s.setString(3,chapter);
				s.setString(4,question);
				s.setString(5,option1);
				s.setString(6,option2);
				s.setString(7,option3);
				s.setString(8,option4);
				s.setString(9,rtopt);

				s.executeUpdate();

			}
		}else{

			out.println("try again by adding subject or chapter(press any key to continue)");
			out.flush();
			in.readLine();

		}


	}

	private void AddChapters(String user) throws ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub
		DB db= new DB();
		c = db.conn;
		s = db.stmt;

		out.println("Enter Subject name");
		out.flush();

		String subject = in.readLine();
		String chapter;

		PreparedStatement ps = c.prepareStatement("select subject from subject where username=? and subject=?");

		ps.setString(1,user);
		ps.setString(2,subject);

		ResultSet rs = ps.executeQuery();

		if(rs.next())
		{
			out.println("menu\nSubject exists.. add chapters...");
			out.flush();
			out.println("Enter chapter name..");
			out.flush();
			out.println("end");
			out.flush();

			chapter = in.readLine();

			PreparedStatement p = c.prepareStatement("select subject from chapter where username=? and subject=? and chapter=?");

			p.setString(1,user);
			p.setString(2,subject);
			p.setString(3,chapter);

			ResultSet r = p.executeQuery();

			if(r.next()){
				out.println("Chapter already exists.. try adding questions..(press any key to continue)");
				out.flush();
				in.readLine();
			}else{
				PreparedStatement s = c.prepareStatement("insert into chapter values(?,?,?)");

				s.setString(1,user);
				s.setString(2,subject);
				s.setString(3,chapter);
				//p.setString(4,role);

				s.executeUpdate();


			}
		}else{

			out.println("Subject does not exits.. try adding Subject..(press any key to continue)");
			out.flush();
			in.readLine();
		}
	}




	private void AddSubjects(String user) throws ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub

		DB db= new DB();
		c = db.conn;
		s = db.stmt;

		out.println("Enter Subject name");
		out.flush();

		String subject = in.readLine();

		PreparedStatement ps = c.prepareStatement("select subject from subject where username=? and subject=?");

		ps.setString(1,user);
		ps.setString(2,subject);

		ResultSet rs = ps.executeQuery();

		if(rs.next())
		{
			out.println("Subject already exists.. Try adding the questions...(press any key to continue)");
			out.flush();
			in.readLine();
		}
		else{
			PreparedStatement p = c.prepareStatement("insert into subject values(?,?)");

			p.setString(1,user);
			p.setString(2,subject);
			p.executeUpdate();	
		}
	}
}