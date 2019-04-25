#! /bin/octave -qf

clear all, clc, close all;


arg_list = argv();
arg_list_matrix = str2num(cell2mat(arg_list));
##typeinfo(arg_list_matrix)
##InputSize = size(arg_list_matrix)

baseLine = (arg_list_matrix((2*length(arg_list_matrix)/3) + 1:end));
##BaseLineSize = size(BaseLine)

sampleOne = arg_list_matrix(1:(length(arg_list_matrix)/3));
##SampleOneSize = size(sampleOne)

sampleTwo = (arg_list_matrix((length(arg_list_matrix)/3) + 1:(2*length(arg_list_matrix))/3));
##SampleTwoSize = size(sampleTwo)

absorbanceOne = log10(baseLine./sampleOne).';

absorbanceTwo = log10(baseLine./sampleTwo).';

absorbanceMixed = log10(baseLine./mixedSample).';


sampleData = [absorbanceOne, absorbanceTwo];
##SizeOfSum = size(SumOfSamples)

x = absorbanceMixed\sampleData;

Conc1 = (x(1)/sum(x));
Conc2 = (1 - Conc1) * 100
Conc1 = Conc1 * 100

