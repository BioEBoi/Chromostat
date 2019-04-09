#! /bin/octave -qf


arg_list = argv ();

sampleOne = cell2mat(arg_list(1:length(arg_list)/3)).';
length(arg_list)
sampleOne
sampleTwo = cell2mat(arg_list((length(arg_list)/3) + 1:(2*length(arg_list))/3)).';
sampleTwo
BaseLine = cell2mat(arg_list((2*length(arg_list)/3) + 1:end)).';
BaseLine
SumOfSamples = [sampleOne(1,1) , sampleOne(1,2); sampleTwo(2,1), sampleTwo(2,2)];
SumOfSamples
size(SumOfSamples)
size(BaseLine)
BaseLine

 relativeConcentration = BaseLine/(SumOfSamples)

