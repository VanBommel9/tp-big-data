package org.epf.hadoop.colfil1;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class Job1Mapper extends Mapper<Object, Text, Text, Text> {
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String[] tokens = value.toString().split("\t");
        if (tokens.length == 3) {
            String user1 = tokens[0];
            String user2 = tokens[1];
            String weight = tokens[2];
            context.write(new Text(user1), new Text(user2 + ":" + weight));
            context.write(new Text(user2), new Text(user1 + ":" + weight));
        }
    }
}
