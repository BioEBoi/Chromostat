#!/usr/bin/octave -qfw
#
arg_input = argv();

typeinfo(arg_input)

x = cell2mat(arg_input) * 2;

typeinfo(x)

x = int2str(x);

typeinfo(x);

printf(x);

