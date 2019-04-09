#! /bin/octave -qf


arg_list = argv ();

sampleOne = cell2mat(arg_list(1:length(arg_list)/3));
length(arg_list)
sampleOne
sampleTwo = cell2mat(arg_list((length(arg_list)/3):(2*length(arg_list))/3));
sampleTwo
BaseLine = cell2mat(arg_list((2*length(arg_list)/3):end));
BaseLine
SumOfSamples = [sampleOne;sampleTwo];

 relativeConcentration = BaseLine/(SumOfSamples)

