package com.sunny.ml.trials;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.*;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

import javax.swing.*;

/**
 * Created by sundas on 11/10/2017.
 */
public class ClassificationTrial {


  /**
   *
   * @param dataSetFile
   */
  public static void classificationTrialOnZooAnimals(String dataSetFile)throws Exception{
    ConverterUtils.DataSource dataSource = new ConverterUtils.DataSource(dataSetFile);
    Instances instances = dataSource.getDataSet();
    System.out.println("Number of instances loaded = " + instances.size());
    //System.out.println("Data loaded = " + instances.toString());
    Remove remove = new Remove();
    // Remove attributes
    String[] opts = new String[]{ "-R", "1"};
    remove.setOptions(opts);
    remove.setInputFormat(instances);
    instances = Filter.useFilter(instances, remove);
    System.out.println("Data cleansed = " + instances);
    // Use information gain and rank
    InfoGainAttributeEval eval = new InfoGainAttributeEval();
    Ranker search = new Ranker();
    weka.attributeSelection.AttributeSelection attSelect = new AttributeSelection();
    attSelect.setEvaluator(eval);
    attSelect.setSearch(search);
    attSelect.SelectAttributes(instances);
    int[] indices = attSelect.selectedAttributes();
    System.out.println("Indices dump " + Utils.arrayToString(indices));
    // Build decision tree
    J48 tree = new J48();
    String[] options = new String[1];
    options[0] = "-U";
    tree.setOptions(options);
    tree.buildClassifier(instances);
    System.out.println(tree);
    // Visualizer
   /* TreeVisualizer tv = new TreeVisualizer(null, tree.graph(), new PlaceNode2());
    JFrame frame = new javax.swing.JFrame("Tree Visualizer");
    frame.setSize(800, 500);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(tv);
    frame.setVisible(true);
    tv.fitToScreen();*/
    // new Classifier
    double[] vals = new double[instances.numAttributes()];
    vals[0] = 1.0;  //hair {false, true}
    vals[1] = 0.0;  //feathers {false, true}
    vals[2] = 0.0;  //eggs {false, true}
    vals[3] = 1.0;  //milk {false, true}
    vals[4] = 0.0;  //airborne {false, true}
    vals[5] = 0.0;  //aquatic {false, true}
    vals[6] = 0.0;  //predator {false, true}
    vals[7] = 1.0;  //toothed {false, true}
    vals[8] = 1.0;  //backbone {false, true}
    vals[9] = 1.0;  //breathes {false, true}
    vals[10] = 1.0;  //venomous {false, true}
    vals[11] = 0.0;  //fins {false, true}
    vals[12] = 4.0;  //legs INTEGER [0,9]
    vals[13] = 1.0;  //tail {false, true}
    vals[14] = 1.0;  //domestic {false, true}
    vals[15] = 0.0;  //catsize {false, true}
    Instance instance = new DenseInstance(1.0,vals);
    instance.setDataset(instances);
    double result = tree.classifyInstance(instance);
    System.out.println("What is the classification of my instance ? Ans: "
        + instances.classAttribute().value((int) result));
    Classifier cl = new J48();
    Evaluation eval_roc = new Evaluation(instances);
    eval_roc.crossValidateModel(cl, instances, 10, new Debug.Random(1), new Object[] {});
    System.out.println(eval_roc.toSummaryString());
    double[][] confusionMatrix = eval_roc.confusionMatrix();System.out.println(eval_roc.toMatrixString());
  }

  /**
   *
   * @param args
   */
  public static void main(String[] args)throws Exception {
    String dataSourceFile = "zoo.arff";
    classificationTrialOnZooAnimals(dataSourceFile);
  }
}
