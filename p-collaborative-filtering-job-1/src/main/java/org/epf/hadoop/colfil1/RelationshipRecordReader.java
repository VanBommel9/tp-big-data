package org.epf.hadoop.colfil1;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;
import java.util.Scanner;

public class RelationshipRecordReader extends RecordReader<LongWritable, Text> {
    private Scanner scanner;
    private LongWritable currentKey = new LongWritable();
    private Text currentValue = new Text();
    private long position = 0;

    @Override
    public void initialize(InputSplit split, TaskAttemptContext context) throws IOException {
        scanner = new Scanner(split.getPath().getFileSystem(context.getConfiguration()).open(split.getPath()));
    }

    @Override
    public boolean nextKeyValue() {
        if (scanner.hasNextLine()) {
            currentKey.set(position++);
            currentValue.set(scanner.nextLine());
            return true;
        }
        return false;
    }

    @Override
    public LongWritable getCurrentKey() {
        return currentKey;
    }

    @Override
    public Text getCurrentValue() {
        return currentValue;
    }

    @Override
    public float getProgress() {
        return 0;
    }

    @Override
    public void close() {
        scanner.close();
    }
}
