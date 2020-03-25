from builtins import *

from pydantic import BaseSettings


class A(BaseSettings):
    b: str


A().<caret>
