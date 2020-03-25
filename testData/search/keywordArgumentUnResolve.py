from pydantic import BaseModel


class A(BaseModel):
    cde: str


A(ab<caret>c='cde')
# count 0