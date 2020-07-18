import React, {Component} from "react";
import { css } from "@emotion/core";
import BounceLoader from "react-spinners/BounceLoader"
import { Container } from "react-bootstrap";

const override = css`
    display: grid;
    margin: auto;
    // position: absolute;
    text-align: center;
`;

export default class Spinner extends Component {
    constructor(props) {
        super(props);
        this.state = { loading: true };
    }

    render() {
        return (
            <Container className="spinner">
                <BounceLoader
                    css={override}
                    size={40}
                    color={"#0031d6"}
                    loading={this.state.loading}
                />
                <p className="text-center">Loading</p>
            </Container>
        )
    }

}


