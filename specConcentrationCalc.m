#! /bin/octave -qf

clear all, clc, close all;

arg_list = argv();
arg_list_matrix = cell2mat(arg_list).'

sampleOne = (arg_list_matrix(1:length(arg_list)/3))
##length(arg_list)
##sampleOne
sampleTwo = cell2mat(arg_list((length(arg_list_matrix)/3) + 1:(2*length(arg_list_matrix))/3))
##sampleTwo
SumOfSamples = [sampleOne; sampleTwo]
BaseLine = (arg_list_matrix((2*length(arg_list_matrix)/3) + 1:end))

##BaseLine

##SumOfSamples
##size(SumOfSamples)
##size(BaseLine)

  
 relativeConcentration = BaseLine * inverse(SumOfSamples)

