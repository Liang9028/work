package text;


	import java.io.BufferedReader;
	import java.io.File;
	import java.io.FileInputStream;
	import java.io.FileNotFoundException;
	import java.io.IOException;
	import java.io.InputStreamReader;
    import java.util.Scanner;
	 
	public class Wordcount {
	    public void  Wordcountexe() throws IOException{
	        System.out.println("请输入查找的文件位置：");
	    	Scanner sc= new Scanner(System.in); 
	    	String filepath=sc.nextLine();
	        BufferedReader br =null;
	        int countWord=0;
	        int countChar=0;
	        int countLine=0;
	        String s="";
	        String strCount="";
	        try {
	             br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filepath))));
	             while((s=br.readLine())!=null)
	              {
	                 s=s+" ";
	                 strCount+=s;
	               countLine++;
	              }
	             for(int i=0;i<strCount.split(" ").length;i++){
	                 if(!strCount.split(" ")[i].equals(" "))
	                     countWord++;
	                 countChar+= strCount.split(" ")[i].length();
	             }
	             System.out.println("单词数："+countWord);
	             System.out.println("字符数："+countChar);
	             System.out.println("行数："+countLine);
	        } catch (FileNotFoundException e) {
	           
	            e.printStackTrace();
	        }finally{
	            br.close();
	        }
	    }
	    public static void main(String[] args) throws IOException{
	       Wordcount w=new Wordcount();
	        w. Wordcountexe();
	         
	    }
	}

