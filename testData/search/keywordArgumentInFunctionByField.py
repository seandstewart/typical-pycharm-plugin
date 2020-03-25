from pydantic import BaseModel


class A(BaseModel):
    abc: str # expected

def func(instance: A):
    instance(ab<caret>c)
## count: 2