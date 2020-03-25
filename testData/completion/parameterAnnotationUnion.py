from builtins import *
from pydantic import BaseModel
from typing import Union


class A(BaseModel):
    abc: str
    cde: str = str('abc')
    efg: str = str('abc')


def get_a(a: Union[A, str]):
    a.<caret>