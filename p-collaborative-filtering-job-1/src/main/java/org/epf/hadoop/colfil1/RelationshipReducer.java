package org.epf.hadoop.colfil1;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;
import java.util.HashSet;

public class RelationshipReducer extends Reducer<Text, Text, Text, Text> {
    private Text result = new Text();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        // Utiliser un Set pour éviter les doublons
        HashSet<String> uniqueRelations = new HashSet<>();

        for (Text value : values) {
            uniqueRelations.add(value.toString());
        }

        // Convertir le Set en une chaîne de caractères séparés par des virgules
        String output = String.join(",", uniqueRelations);
        result.set(output);

        // Écrire le résultat pour la clé (utilisateur)
        context.write(key, result);
    }
}