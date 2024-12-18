import PropTypes from "prop-types";
import Rating from '@mui/material/Rating';

export const HallCard = (props) => {
    return (
        <>
            <div className="rounded-md overflow-hidden shadow-lg">
                <div
                    className={"relative"}>
                    <img
                        src={props.image || "/assets/WorkSpace.jpg"}
                        alt="hall"
                        className="w-full object-fill aspect-video min-h-[220px]"
                    />
                </div>
                <div className={"bg-white p-4 min-h-[80px]"}>
                    <h1 className="text-lg font-semibold">{props.Name}</h1>
                    <Rating name="read-only" value={props.stars} readOnly />
                </div>
            </div>
        </>
    )
}
HallCard.propTypes = {
    image: PropTypes.string,
    Name: PropTypes.string,
    stars: PropTypes.number
}