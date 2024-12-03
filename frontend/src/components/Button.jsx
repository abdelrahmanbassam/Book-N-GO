import PropTypes from "prop-types";

export const Button = (props) => {
    return (
        <>
            <button
                className={"px-4 py-2 tr bg-secondary2 text-primary w-[100px] h-[40px] hover:bg-secondary2Hover hover:text-text " + props.className }
                onClick={props.onClick}
            >
                {props.children}
            </button>
        </>
    )
}

Button.propTypes = {
    className: PropTypes.string,
    onClick: PropTypes.func,
    children: PropTypes.node,
};