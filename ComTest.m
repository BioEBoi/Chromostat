#!/usr/bin/octave -qfw

arg_input = argv();

disp(arg_input)

x = cell2mat(arg_input{1,1}) .* 2;

disp(x);

