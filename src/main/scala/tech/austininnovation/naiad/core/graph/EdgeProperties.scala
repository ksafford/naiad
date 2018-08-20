package tech.austininnovation.naiad.core.graph

/*
 I don't like this approach. I would like to have one list of properties that can
 have values on any type. However, a shapeless HList would allow any type at all
 in the list, and I want this restricted to be a list of only EdgeProperty.

 So for now, two lists it is.
 */
//case class EdgeProperties(properties: (Option[List[EdgePropertyD]], Option[List[EdgePropertyS]]))
case class EdgeProperties(properties: List[EdgeProperty])