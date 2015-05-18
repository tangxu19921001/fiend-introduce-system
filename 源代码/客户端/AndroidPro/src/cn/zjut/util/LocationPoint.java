package cn.zjut.util;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationPoint implements Parcelable {
	private float x, y;

	public LocationPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public LocationPoint() {
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeFloat(x);
		dest.writeFloat(y);
	}

	public static final Parcelable.Creator<LocationPoint> CREATOR = new Creator<LocationPoint>() {
		public LocationPoint createFromParcel(Parcel source) {
			LocationPoint lp = new LocationPoint(0.0f,0.0f);
			lp.x= source.readFloat();
			lp.y = source.readFloat();
			return lp;
		}

		public LocationPoint[] newArray(int size) {
			return new LocationPoint[size];
		}
	};

}
