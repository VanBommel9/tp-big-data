package org.epf.hadoop.colfil3;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecommendationReducer extends Reducer<Text, Text, Text, Text> {

    private Text result = new Text();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        List<String> recommendations = new ArrayList<>();

        for (Text value : values) {
            recommendations.add(value.toString());
        }

        // Sort recommendations by count (descending) and lexicographical order
        Collections.sort(recommendations, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String[] parts1 = o1.split(":");
                String[] parts2 = o2.split(":");
                int count1 = Integer.parseInt(parts1[1]);
                int count2 = Integer.parseInt(parts2[1]);

                if (count1 != count2) {
                    return count2 - count1; // Descending order of count
                }
                return parts1[0].compareTo(parts2[0]); // Lexicographical order
            }
        });

        // Keep only the top 5 recommendations
        StringBuilder topRecommendations = new StringBuilder();
        for (int i = 0; i < Math.min(5, recommendations.size()); i++) {
            if (i > 0) {
                topRecommendations.append(", ");
            }
            topRecommendations.append(recommendations.get(i));
        }

        result.set(topRecommendations.toString());
        context.write(key, result);
    }
}