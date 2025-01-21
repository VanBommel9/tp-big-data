package org.epf.hadoop.colfil3;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class RecommendationMapper extends Mapper<Object, Text, Text, Text> {

    private Text userKey = new Text();
    private Text recommendationValue = new Text();

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // Parse input: "alice,david    3"
        String[] parts = value.toString().split("\\t");
        String[] users = parts[0].split(",");
        String count = parts[1];

        // Emit both directions
        userKey.set(users[0]);
        recommendationValue.set(users[1] + ":" + count);
        context.write(userKey, recommendationValue);

        userKey.set(users[1]);
        recommendationValue.set(users[0] + ":" + count);
        context.write(userKey, recommendationValue);
    }
}