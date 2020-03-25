from pydantic import BaseModel


class A(BaseModel):
    abc: str # expected

class B(A):
    pass

class C(B):
    ab<caret>c: str # expected

A(abc='cde')
B(abc='cde')
C(abc='cde') # expected
## count: 3