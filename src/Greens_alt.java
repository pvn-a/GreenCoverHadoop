import java.io.*;
import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Greens_alt {

  public static class GreensMapper extends Mapper<Object, Text, Text, DoubleWritable>{

    public void map(Object key, Text value,Context context) throws IOException, InterruptedException {
    
    	String area = new String("");
        String green = new String("");
    	StringTokenizer st = new StringTokenizer(value.toString());
    	st.nextToken(); 			// skipping first token i.e name of the image file
    	while (st.hasMoreTokens()) 
    	{
    	    area = st.nextToken();
       		green = st.nextToken();
       		Double greenvalue = Double.parseDouble(green); 
       		if(greenvalue > 75){
       			context.write(new Text(area), new DoubleWritable(greenvalue));
       		}
   		}
    }
  }

  public static class GreensReducer extends Reducer<Text,DoubleWritable,Text,DoubleWritable> {
  
    public void reduce(Text key,DoubleWritable values,Context context) throws IOException, InterruptedException {
    
     	context.write(key,values);
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    
    Job job = Job.getInstance(conf, "greens");
    job.setJarByClass(Greens_alt.class);
    
    job.setMapperClass(GreensMapper.class);
    // job.setCombinerClass(GreensReducer.class);
    job.setReducerClass(GreensReducer.class);
    
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(DoubleWritable.class);
    
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
