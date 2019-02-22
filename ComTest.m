#!/usr/bin/octave -qfw

arg_input = argv();

x = cell2mat(arg_input) .* 2;

disp(x);

