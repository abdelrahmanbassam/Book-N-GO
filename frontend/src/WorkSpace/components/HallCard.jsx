import {Rate} from "../../components/Rate";
import PropTypes from "prop-types";

export const HallCard = (props) => {
    return (
        <>
            <div
                style={{
                    minHeight: "200px",
                }}
            >
                <div
                    className={""}>
                    <img
                        src={props.image}
                        alt="hall1"
                        className="w-full object-fill aspect-video"
                        style={{
                            minHeight: "220px",
                        }}
                    />
                </div>
                <div className={"bg-white p-4"}
                        style={{
                            minHeight: "80px",
                        }}>
                    <h1 className="text-lg font-semibold">{props.Name}</h1>
                    <Rate stars={props.stars}/>
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