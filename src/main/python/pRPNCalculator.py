# !/usr/bin/env python

# #
# Created by vivek on 02/07/2014.
##

import sys
import re

# dictionary <operator> - <function>
OPS = {
    '+': (lambda x, y: x + y),
    '-': (lambda x, y: y - x),
    '*': (lambda x, y: x * y),
    '/': (lambda x, y: y / x)
}


def evalTokens(tokens):
    ''' Evaluate RPN expr (given as string of tokens) '''
    stack = []
    for token in tokens:
        if token in OPS:
            stack.append(OPS[token](stack.pop(), stack.pop()))
        else:
            stack.append(float(token))
    return stack.pop()


if __name__ == "__main__":
    while True:
        # Read line by line from stdin + tokenize line + evaluates line
        tokens = re.split(" *", sys.stdin.readline().strip())
        print tokens
        if not tokens:
            break
        sys.stdout.write("Expression: %2.2fn" % evalTokens(tokens))