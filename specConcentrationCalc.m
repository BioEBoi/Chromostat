#! /bin/octave -qf


arg_list = argv ();

sampleOne = cell2mat(arg_list(1:length(arg_list)/3));
sampleTwo = cell2mat(arg_list((length(arg_list)/3):(2*length(arg_list))/3));
BaseLine = cell2mat(arg_list((2*length(arg_list)/3):end));
SumOfSamples = [sampleOne;sampleTwo];

 relativeConcentration = BaseLine/(SumOfSamples)

