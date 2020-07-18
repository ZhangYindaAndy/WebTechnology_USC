import React from "react";

import { Badge } from "react-bootstrap";


const renderTag = (tagName) => {
    tagName = tagName.toUpperCase();
    if(tagName === "WORLD")
        return (
            <Badge className="ml-1" style={{backgroundColor: "#7c4efe", color: "white"}}>{tagName}</Badge>
        );
    else if(tagName === "POLITICS")
        return (
            <Badge className="ml-1" style={{backgroundColor: "#419487", color: "white"}}>{tagName}</Badge>
        );
    else if(tagName === "BUSINESS")
        return (
            <Badge className="ml-1" style={{backgroundColor: "#4696ee", color: "white"}}>{tagName}</Badge>
        );
    else if(tagName === "Technology")
        return (
            <Badge className="ml-1" style={{backgroundColor: "#cedc37", color: "black"}}>{tagName}</Badge>
        );
    else if(tagName === "SPORT" || tagName === "SPORTS")
        return (
            <Badge className="ml-1" style={{backgroundColor: "#f6c240", color: "black"}}>{tagName}</Badge>
        );
    else if(tagName === "GUARDIAN")
        return (
            <Badge className="ml-1" style={{backgroundColor: "#14283c", color: "white"}}>{tagName}</Badge>
        );
    else if(tagName === "NYTIMES")
        return (
            <Badge className="ml-1" style={{backgroundColor: "#dddddd", color: "black"}}>{tagName}</Badge>
        );
    else
        return (
            <Badge className="ml-1" style={{backgroundColor: "#6e757f", color: "white"}}>{tagName}</Badge>
        );
}

export default renderTag;