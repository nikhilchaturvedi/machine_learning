/**
 * @author Nikhil Chaturvedi
 * */

package com.learning.perceptron;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;

import org.math.plot.*;

public class Perceptron {

	float V[] = new float[3];
	Map<float[], Integer> X = null;
	float w[] = new float[3];
	
	void init(int n)
	{
		float xA = randomNumbers(-1, 1);
		float yA = randomNumbers(-1, 1);
		float xB = randomNumbers(-1, 1);
		float yB = randomNumbers(-1, 1);
		
		V[0] = (xB*yA - xA*yB);
		V[1] = yB-yA;
		V[2] = xA-xB;
		X = generatePoints(n);
		
	}
	
	Map<float[], Integer> generatePoints(int n)
	{
		HashMap<float[], Integer> X = new HashMap<float[], Integer>();
		int sign  = 0;
		float x1 = 0;
		float x2 = 0;
		float s = 0;
		
		for(int i = 0; i < n; i ++)
		{
			x1 = randomNumbers(-1, 1);
			x2 = randomNumbers(-1, 1);
			float x[] = {1, x1, x2};
			s = dot(V, x);
			sign = sign(s);
			X.put(x, sign);
		}
		
		return X;
	}
	
	float classificationError(float[] vec)
	{
		Map<float[], Integer> pts = X;
		int M = pts.size();
		int nMispts = 0;
		float error = 0;
		
		Set<Entry<float[], Integer>> set = X.entrySet();
		Iterator<Entry<float[], Integer>> it = set.iterator();
		
		for(int i = 0; i < M; i++)
		{
			float[] key = it.next().getKey();
			float dot = dot(vec, key);
			int sign = sign(dot);
			
			if(sign != pts.get(key))
			{
				nMispts += 1;
			}
		}
		error = nMispts / (float)M;
		
		return error;
	}
	
	Map<float[], Integer> chooseMiscPoint(float[] vec)
	{
		Map<float[], Integer> pts = X;
		Map<float[], Integer> misPts = new HashMap<float[], Integer>();
		
		Set<Entry<float[], Integer>> set = X.entrySet();
		Iterator<Entry<float[], Integer>> it = set.iterator();
		
		int M = pts.size();
		
		for(int i = 0; i < M; i++)
		{
			float[] key = it.next().getKey();
			float dot = dot(vec, key);
			int sign = sign(dot);
			
			if(sign != pts.get(key))
			{
				misPts.put(key, pts.get(key));
			}
		}
		
		Set<Entry<float[], Integer>> misSet = misPts.entrySet();
		Iterator<Entry<float[], Integer>> misIt = misSet.iterator();
		float retArr[] = null;
		int retVal = 0;
		
		while(misIt.hasNext())
		{
			Entry<float[], Integer> entry = misIt.next();
			retArr = entry.getKey();
			retVal = entry.getValue();
		}
		
		Map<float[], Integer> retMap = new HashMap<float[], Integer>();
		retMap.put(retArr, retVal);
		
		return retMap;
	}
	
	void PLA()
	{
		float w[] = {0, 0};
		Map<float[], Integer> map = null;
		
		while(classificationError(w) != 0)
		{
			
			map = chooseMiscPoint(w);
			
			float[] x = map.keySet().iterator().next();
			int s = map.get(x);
			
			for(int i = 0; i < x.length; i++)
			{
				x[i] = x[i] * s;
			}
			
			for(int i = 0; i < w.length; i++)
			{
				w[i] = w[i] + (s * x[i]);
			}
		}
		
		this.w = w;
		System.out.println("Updated errors "+Arrays.toString(w));
		plot(w);
	}
	
	void checkError()
	{
		generatePoints(20);
		
	}
	
	void plot(float[] vec)
	{
		float[] V = this.V;
		float a = -V[0]/V[1];
		float b = -V[0]/V[1];
		
		double[] l = new double[50];
		double[] templ = new double[50];
		float up = 1;
		float low = -1;
		int length = 50;
		float step = (up - low) / (float)length;
		for(int i = 0; i < length; i ++)
		{
			l[i] = low;
			low = low + step;
			//System.out.println("Printing l[i] "+l[i]);
		}
		
		for(int i = 0; i < length; i++)
		{
			templ[i] = l[i] * a + b;
		}
		
		Plot2DPanel panel = new Plot2DPanel();
		panel.addScatterPlot("Perceptron", l, templ);
		
		JFrame frame = new JFrame("Frame");
		frame.setContentPane(panel);
		frame.setVisible(true);
		
		Color[] colors = {Color.red, Color.blue};
		Map<float[], Integer> X = this.X;
		
		Iterator<float[]> it = X.keySet().iterator();
		
		while(it.hasNext())
		{
			
			float[] x = it.next();
			int s = X.get(x);
			panel.repaint((int)x[1], (int)x[2], 100, 100);
			
		}
		
		float aa = -vec[1]/vec[1];
		float bb = -vec[0]/vec[1];
		
		double[] templl = new double[50];
		
		for(int i = 0; i < length; i++)
		{
			templl[i] = l[i] * aa + bb;
		}
		panel.addScatterPlot("Second", l, templl);
		
	}
	
	public static int randRange(int min, int max)
	{
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	float dot(float[] a, float[] b)
	{
		int n = a.length;
		float sum = 0;
		
		for(int i = 0; i < n; i++)
		{
			sum += a[i] * b[i];
		}
		
		return sum;
	}
	
	int sign(double s)
	{
		if(s < 0)
			return -1;
		else if(s == 0.0)
			return 0;
		else
			return 1;
	}
	
	float randomNumbers(int min, int max)
	{
		return (min + (float)(Math.random() * ((max - min) + 1)));
	}
	
}
