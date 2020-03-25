from builtins import *
from typing import Union


class A:
    abc: str
    cde: str = str('abc')
    efg: str = str('abc')


def get_a(a: Union[A, str]):
    a.<caret>