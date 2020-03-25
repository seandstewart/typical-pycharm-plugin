from builtins import *


class A:
    abc: str
    cde = str('abc')
    efg: str = str('abc')

class B(A):
    hij: str

a = A()
a.<caret>