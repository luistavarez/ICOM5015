import java.util.ArrayList;


public class AlphaBeta {
	private int value;
	private int ag;
	private ArrayList<Agent> agentList = new ArrayList<>();

	/*
	private void uploadList(Agent agList[]){
		for(int i = 0 ; i < 6 ; i++)
			agentList[i] = agList[i];
	}*/
	private int getValue() {
		return value;
	}
	private void setValue(int value) {
		this.value = value;
	}
	private int getAgent() {
		return ag;
	}
	private void setAg(int ag) {
		this.ag = ag;
	}
	public AlphaBeta() {
		// TODO Auto-generated constructor stub
	}
	public void prunn(int node,int depth){
		int[]v = this.AB_pruning(node, depth, -1000000, 1000000, 0, agentList);
		this.setValue(v[0]);
		this.setAg(v[1]);
	}
	public int[] AB_pruning(int node, int depth, int alpha, int beta,int step, ArrayList<Agent> agentList){
		int[] v = {-1000000,6};
		ArrayList<Agent> agentL = agentList;
		if(agentL.get((node+step)%6).lost()) {
			return this.AB_pruning(node,depth,alpha,beta,step+1,agentL);
		}
		else if(depth == 0){
			
			int turns = (int)(step / 6)+1;
			v[0] = agentL.get((node+step)%6).getTurnScore(agentL.get((node+step)%6).getEssentialResQty(),
					agentL.get((node+step)%6).getDesirableResQty(),agentL.get((node+step)%6).getLuxuryResQty(),turns,null);
			v[0] = ((agentL.get((node+step)%6).getDesirableResQty()-turns < 0)||
					(agentL.get((node+step)%6).getEssentialResQty()-(3*turns) < 0)||
					(agentL.get((node+step)%6).getLuxuryResQty()<0)?-1000000:v[0]);
			v[1] = node;
			return v;
		}
		else if(step%6 == 0){
			v[0] = -1000000;
			v[1] = 6;
			for(int i = 0;i < 7;i++){
				agentL = agentList;
				if(i==6 || ( (agentL.get(i).getLuxuryRes().equals(agentL.get((node+step)%6).getEssentialRes()) 
						|| agentL.get(i).getLuxuryRes().equals(agentL.get((node+step)%6).getDesirableRes())) 
						&& agentL.get(i).getEssentialRes().equals(agentL.get((node+step)%6).getLuxuryRes()))) {
					if(i != 6 && agentL.get((node+step)%6).canTrade() && agentL.get(i).canTrade()) {
						agentL.set(i, agentL.get((node+step)%6).trade(agentL.get(i),agentL.get(i).getLuxuryRes()));
					}
					int vTemp[] = this.AB_pruning(node,depth-1,alpha,beta,step+1,agentL);
					v[0] = Math.max(v[0],vTemp[0]);
					if(alpha<v[0]){
						alpha = v[0];
						v[1] = i;
					}
					if(beta <= alpha)
						break;
				}
			}
			return v;
		}

		else{
			v[0] = 1000000;
			v[1] = 6;
			for(int i = 0;i < 7;i++){
				agentL = agentList;
				if(i==6 || ((agentL.get(i).getLuxuryRes().equals(agentL.get((node+step)%6).getEssentialRes())  
						|| agentL.get(i).getLuxuryRes().equals(agentL.get((node+step)%6).getDesirableRes())) 
						&& agentL.get(i).getEssentialRes().equals(agentL.get((node+step)%6).getLuxuryRes()))) {
					if(i != 6 && agentL.get((node+step)%6).canTrade() && agentL.get(i).canTrade()) {
						//agentL[i] = agentL[(node+step)%6].trade(agentL[i],agentL[i].getLuxuryRes());
					}
					int vTemp[] = this.AB_pruning(node,depth-1,alpha,beta,step+1,agentL);
					//vTemp[0] = vTemp[0] - this.AB_pruning(node,0,alpha,beta,step,agentL)[0];
					v[0] = Math.min(v[0],vTemp[0]);
					if(beta>v[0]){
						beta = v[0];
						v[1] = i;
					}
					if(beta <= alpha)
						break;
				}
			}
			return v;
		}
	}

}


