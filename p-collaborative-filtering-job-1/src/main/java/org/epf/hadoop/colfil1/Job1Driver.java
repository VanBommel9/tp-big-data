package org.epf.hadoop.colfil1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

public class Job1Driver {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: Job1Driver <input path> <output path>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Job 1: Relationship Aggregation");

        job.setJarByClass(Job1Driver.java);
        job.setMapperClass(Job1Mapper.java);
        job.setReducerClass(Job1Reducer.java);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setInputFormatClass(RelationshipInputFormat.class);

        RelationshipInputFormat.addInputPath(job, new Path(args[0]));
        RelationshipInputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
