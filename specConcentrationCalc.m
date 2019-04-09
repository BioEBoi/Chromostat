#! /bin/octave -qf


arg_list = argv ();

sampleOne = arg_list(1:length(arg_list)/3);
sampleTwo = arg_list((length(arg_list)/3):(2*length(arg_list))/3);
BaseLine = arg_list((2*length(arg_list)/3):end);
SumOfSamples = [sampleOne;sampleTwo];

 relativeConcentration = BaseLine\(SumOfSamples)

