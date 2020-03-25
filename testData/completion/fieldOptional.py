from builtins import *
from pydantic import BaseModel
from typing import Optional


class A(BaseModel):
    abc: Optional[str]
    cde = str('abc')
    efg: str = str('abc')

class B(A):
    hij: str

A().<caret>