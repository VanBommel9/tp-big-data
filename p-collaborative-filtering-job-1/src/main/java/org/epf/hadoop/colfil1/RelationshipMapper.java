package org.epf.hadoop.colfil1;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class RelationshipMapper extends Mapper<LongWritable, Relationship, Text, Text> {
    private Text userKey = new Text();
    private Text userValue = new Text();

    @Override
    protected void map(LongWritable key, Relationship value, Context context) throws IOException, InterruptedException {
        // Extraire les identifiants des utilisateurs
        String id1 = value.getId1();
        String id2 = value.getId2();

        // Émettre A -> B
        userKey.set(id1);
        userValue.set(id2);
        context.write(userKey, userValue);

        // Émettre B -> A
        userKey.set(id2);
        userValue.set(id1);
        context.write(userKey, userValue);
    }
}