package com.xidian.qhh.tia.machinelearning;

import java.util.List;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import com.xidian.qhh.tia.tia.FeatureVector;

/**
 * Created by dell on 2018/4/19.
 */

public class SVMClassifier extends Classifier {

    /**
     * Parameter to the SVM
     */
    svm_parameter parameter;
    /**
     * Learned SVM model
     */
    public svm_model model;
    /**
     * Number of Features for the classifier
     */
    private int numFeatures;
    public final double threshold = 0.5;

    static double minDistance=Double.MAX_VALUE;
    List<FeatureVector> data1;


    public SVMClassifier(int numFeatures)
            throws IllegalArgumentException {
        if (numFeatures < 1)
            throw new IllegalArgumentException("numFeatures must be greater " +
                    "than zero");
        this.numFeatures = numFeatures;

        this.parameter = new svm_parameter();
        setSVMParameter(
                1,
                0.5,
                0.5,
                1,
                svm_parameter.C_SVC,
                svm_parameter.RBF,
                20000,
                0.001
        );
    }



    public void setSVMParameter(int probability, double gamma,
                                double nu, double C, int svmType, int kernelType,
                                double cacheSize, double eps) {
        this.parameter.probability = probability;
        this.parameter.gamma = gamma;
        this.parameter.nu = nu;
        this.parameter.C = C;
        this.parameter.svm_type = svmType;
        this.parameter.kernel_type = kernelType;
        this.parameter.cache_size = cacheSize;
        this.parameter.eps = eps;
    }


    @Override
    public boolean train(List<FeatureVector> data)
            throws IllegalArgumentException{
        data1=data;

        if (data == null || data.size() < 1)
            throw new IllegalArgumentException("Invalid data");

        svm_problem problem = new svm_problem();

        problem.l = data.size();
        problem.y = new double[data.size()];
        problem.x = new svm_node[data.size()][];

        for (int i = 0; i < data.size(); i++){
            problem.x[i] = new svm_node[this.numFeatures];
            for (int j = 0; j < this.numFeatures; j++){
                svm_node node = new svm_node();
                node.index = j;
                node.value = data.get(i).get(j, 0);
                problem.x[i][j] = node;
            }
            problem.y[i] = (double) data.get(i).getClassLabel();
        }
        this.model = svm.svm_train(problem
                , this.parameter);
        if (this.model == null)
            throw new IllegalArgumentException("Malformed data. "
                    + "Failed to train");

        return true;
    }

    @Override
    public int classify(FeatureVector fv) throws IllegalStateException {
        if (this.model == null )
            throw new IllegalArgumentException("Ivalid state of classifier");
        minDistance=Double.MAX_VALUE;
        for (FeatureVector fv1 : data1) {
            double eucDistance = 0;
            for (int i = 0; i < numFeatures; i++)
                eucDistance += Math.pow(fv1.get(i)- fv.get(i), 2);
            eucDistance = Math.sqrt(eucDistance);



            double myDistance = 0;
            for (int i = 0; i < numFeatures; i++){
                if(i!=4&&i!=5){
                    myDistance += Math.pow(fv1.get(i)- fv.get(i), 2);
                }
            }
            if(myDistance<minDistance){
                minDistance=myDistance;
            }



        }
        if(minDistance>30000){
            return -1;
        }

        svm_node[] nodes = new svm_node[this.numFeatures];
        for (int i = 0; i < this.numFeatures; i++) {
            svm_node node = new svm_node();
            node.index = i;
            node.value = fv.get(i, 0);
            nodes[i] = node;
        }

        int totalClasses = 2;
        int[] labels = new int[totalClasses];
        svm.svm_get_labels(this.model, labels);

        double[] prob_estimates = new double[totalClasses];
        svm.svm_predict_probability(this.model, nodes, prob_estimates);
        if (prob_estimates[0] >= prob_estimates[1])
            return 1;
        else
            return -1;
    }


    public Object getModel() {
        return this.model;
    }
    public static int minDistance(){
        return (int)minDistance;
    }
}