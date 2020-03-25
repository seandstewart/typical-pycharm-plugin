from builtins import *
from pydantic import BaseModel
from typing import Union


class A(BaseModel):
    abc: Union[str, int]
    cde: Union[str, int] = ...
    efg: Union[str, int, None]


class B(A):
    hij: str

A().<caret>