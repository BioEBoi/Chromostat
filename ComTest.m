#!/usr/bin/octave -qfw

arg_input = argv();

disp(arg_input)

x = cell2mat(arg_input) .* 2;

disp(x);

