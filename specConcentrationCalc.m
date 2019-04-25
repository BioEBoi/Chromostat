#! /bin/octave -qf

clear all, clc, close all;


arg_list = argv();
arg_list_matrix = str2num(cell2mat(arg_list));
##typeinfo(arg_list_matrix)
##InputSize = size(arg_list_matrix)


baseLine = arg_list_matrix(1: end/4);
BaseLineSize = size(baseLine)

sampleOne = arg_list_matrix(end/4 + 1: 2 * end/4);
##SampleTwoSize = size(sampleTwo)

sampleTwo = arg_list_matrix(2 * end/4 + 1: 3 * end/4);
##SampleOneSize = size(sampleOne)

mixedSample = arg_list_matrix(3 * end/4 + 1: end);

absorbanceOne = log10(baseLine./sampleOne).';
size(absorbanceOne)

absorbanceTwo = log10(baseLine./sampleTwo).';

absorbanceMixed = log10(baseLine./mixedSample).';


sampleData = [absorbanceOne , absorbanceTwo];
SizeOfSum = size(sampleData)

x = absorbanceMixed\sampleData

##Conc1 = (x(1)/sum(x));
##Conc2 = (1 - Conc1) * 100
##Conc1 = Conc1 * 100
