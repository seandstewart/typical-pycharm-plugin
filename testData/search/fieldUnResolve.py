from pydantic import BaseModel


class A(BaseModel):
    cd<caret>e: str # expected


A(abc='cde')
# count 1