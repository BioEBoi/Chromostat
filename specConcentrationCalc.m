#! /bin/octave -qf

clear all, clc, close all;


arg_list = argv();
arg_list_matrix = cell2mat(arg_list).'
typeinfo(arg_list_matrix)
InputSize = size(arg_list_matrix)

sampleOne = (arg_list_matrix(1:length(arg_list)/3))
SampleOneSize = size(sampleOne)
##length(arg_list)
##sampleOne
sampleTwo = (arg_list_matrix((length(arg_list_matrix)/3) + 1:(2*length(arg_list_matrix))/3))
SampleTwoSize = size(sampleTwo)
##sampleTwo
SumOfSamples = [sampleOne; sampleTwo]
SizeOfSum = size(SumOfSamples)
BaseLine = (arg_list_matrix((2*length(arg_list_matrix)/3) + 1:end))
BaseLineSize = size(BaseLine)

##BaseLine

##SumOfSamples
##size(SumOfSamples)
##size(BaseLine)

  
 relativeConcentration = BaseLine/(SumOfSamples)

