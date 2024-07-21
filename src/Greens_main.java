import java.io.*;
import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Greens_main {

  public static class GreensMapper extends Mapper<Object, Text, Text, IntWritable>{

	private final static IntWritable one = new IntWritable(1);      //Initializing a IntWritable instance with value 1, which will be our value
	private Text area = new Text("Total_areas:");                   //Initializing a text instance with value "Total_areas:" , which will be our key 
	
    public void map(Object key, Text value,Context context) throws IOException, InterruptedException {
    
      String green = new String("");      // Dummy string to temporarily store green% value
    	StringTokenizer st = new StringTokenizer(value.toString());     //tokinizing the input string
    	st.nextToken(); 			              // skipping the first token i.e. name of the image file
    	st.nextToken();				              // skipping the second token i.e. area name
      green = st.nextToken();             // Storing the green% value in the dummy string
      Double greenvalue = Double.parseDouble(green); 
      if(greenvalue > 75){
       		context.write(area,one);        // If the green% > 75 then the <key,value> pair is outputted
      }
    }
  }

  public static class GreensReducer extends Reducer<Text,IntWritable,Text,IntWritable> {
  
  	private IntWritable result = new IntWritable();                   //Initializing a IntWritable instance, which will be our value
  	
    public void reduce(Text key, Iterable<IntWritable> values,Context context) throws IOException, InterruptedException {
    
    	int sum = 0;
        for (IntWritable val : values) {
        	sum += val.get();                   // Iterating throught the values from map and incrementing the sum variable
        }
      	result.set(sum);                      
      	context.write(key, result);           
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    
    Job job = Job.getInstance(conf, "greens");
    job.setJarByClass(Greens_main.class);
    
    job.setMapperClass(GreensMapper.class);
    job.setCombinerClass(GreensReducer.class);
    job.setReducerClass(GreensReducer.class);
    
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
